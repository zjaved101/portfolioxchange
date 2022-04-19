var express = require('express');
var router = express.Router();
const Image = require('../library/model/images')
const User = require('../library/model/users')
const Portfolio = require('../library/model/portfolio')
const multer = require('multer');
const ImgurStorage = require('multer-storage-imgur');
const path = require('path');
const { Op } = require("sequelize");

const imageFilter = function(req, file, cb) {
    // Accept images only
    if (!file.originalname.match(/\.(jpg|JPG|jpeg|JPEG|png|PNG|gif|GIF)$/)) {
        req.fileValidationError = 'Only image files are allowed!';
        return cb(new Error('Only image files are allowed!'), false);
    }
    cb(null, true);
};

const storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, 'public/images/');
    },

    // By default, multer removes file extensions so let's add them back
    filename: function(req, file, cb) {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
    }
});

const upload = multer({storage: ImgurStorage({ clientId: process.env.IMGUR_CLIENT_ID }), fileFilter: imageFilter});

router.post('/upload', upload.single('image'), async function(req, res, next) {
    console.log(req.file, req.body)

    // need to do this due to retrofit from Java (Android)
    let tags = [];
    if(Array.isArray(req.body.tags)) {
        req.body.tags.forEach(element => {
            tags.push(element.replace(/['"]+/g, ''));
        });
    } else {
        tags.push(req.body.tags.replace(/['"]+/g, ''));
    }
    
    console.log(tags);

    let users = await User.findAll({
        where: {
            id: req.body.userId
        }
    });

    if(!users.length) {
        return res.status(400).send({"success": false, "error": "user id does not exist"});
    }
    let uploadCount = users[0].dataValues.uploadCount;

    // req.file contains information of uploaded file
    // req.body contains information of text fields, if there were any

    if (req.fileValidationError) {
        return res.send({"success": false, "error": req.fileValidationError});
    }
    else if (!req.file) {
        return res.status(400).send({'success': false, 'error': 'Please select an image to upload'});
    }

    await User.update({
        uploadCount: uploadCount + 1,
    }, {
        where: {
            id: req.body.userId
        }
    });

    await Image.create({
        title: req.body.title,
        description: req.body.description,
        UserId: parseInt(req.body.userId),
        imgType: req.file.originalname.split('.')[1],
        // tags: JSON.parse(req.body.tags),
        tags: tags,
        likes: 0,
        likeList: [],
        imgLoc: req.file.link
    });

    res.send({"success": true});
});

router.get('/homepage', async (req, res) => {
    let images = await Image.findAll({
        order: [
            ['likes', 'DESC']
        ]
    });

    if (images.length) {
        images.forEach(element => {
            let likeList = JSON.parse(JSON.stringify(element.dataValues.likeList));
            element.dataValues.currentUserLikes = false;
            if(likeList.includes(parseInt(req.query.userId))) {
                element.dataValues.currentUserLikes = true;
            }
        });
        return res.send({
            "success": true,
            "images": images.slice(parseInt(req.query.index), parseInt(req.query.index) + parseInt(req.query.length))
        });
    } else {
        return res.send({"success": false, "images": []});
    }
});

router.get('/details', async (req, res) => {
    console.log(req.query);
    let images = await Image.findAll({
        where: {
            id: req.query.imageId
        }
    });

    if (images.length) {
        images.forEach(element => {
            let likeList = JSON.parse(JSON.stringify(element.dataValues.likeList));
            element.dataValues.currentUserLikes = false;
            if(likeList.includes(parseInt(req.query.userId))) {
                element.dataValues.currentUserLikes = true;
            }
        });
        return res.send(images[0]);
    } else {
        return res.send({"success": false});
    }
});

router.post("/like", async (req, res) => {
    let images = await Image.findAll({
        where: {
            id: req.body.imageId
        }
    });

    if(!images.length) {
        return res.status(400).send({"success": false, "likes": false, "error": "image id does not exist"});
    }

    let users = await User.findAll({
        where: {
            id:req.body.userId
        }
    });
    if(!users.length) {
        return res.status(400).send({"success:": false, "likes": false, "error": "user id does not exist"});
    }
    let likeCount = users[0].dataValues.likeCount;

    let likeList = JSON.parse(JSON.stringify(images[0].dataValues.likeList));
    let likes = images[0].dataValues.likes;

    if(!likeList.includes(req.body.userId)) {
        console.log("updating likeList")
        likeList.push(parseInt(req.body.userId));
        likes += 1;
    } else {
        console.log("user already liked image");
        return res.send({"success": false, "likes": true, "error": "User has already liked"});
    }

    await Image.update({
        likes: likes,
        likeList: likeList
    }, {
        where: {
            id: req.body.imageId
        }
    });

    await User.update({
        likeCount: likeCount + 1,
    }, {
        where: {
            id: req.body.userId
        }
    });

    return res.send({"success": true, "likes": true});
});

router.post("/dislike", async (req, res) => {
    let images = await Image.findAll({
        where: {
            id: req.body.imageId
        }
    });

    if(!images.length) {
        return res.status(400).send({"success": false, "likes": true, "error": "image id does not exist"});
    }

    let users = await User.findAll({
        where: {
            id:req.body.userId
        }
    });
    if(!users.length) {
        return res.status(400).send({"success:": false, "likes": true, "error": "user id does not exist"});
    }
    let likeCount = users[0].dataValues.likeCount;

    let likeList = JSON.parse(JSON.stringify(images[0].dataValues.likeList));
    let likes = images[0].dataValues.likes;

    console.log(req.body, likeList, likes);

    if(likeList.includes(req.body.userId)) {
        console.log("updating likeList")
        let index = likeList.indexOf(parseInt(req.body.userId));
        likeList.splice(index, parseInt(req.body.userId));
        likes -= 1;
    } else {
        console.log("user has not liked image");
        return res.send({"success": false, "likes": false, "error": "User has not liked"});
    }

    await Image.update({
        likes: likes,
        likeList: likeList
    }, {
        where: {
            id: req.body.imageId
        }
    });

    await User.update({
        likeCount: likeCount - 1,
    }, {
        where: {
            id: req.body.userId
        }
    });

    return res.send({"success": true, "likes": false});
});

router.post('/search', async (req, res) => {
    searchKeys = [];
    req.body.tags.forEach(key => {
        searchKeys.push({
            tags: {
                [Op.substring]: key
            }
        });
    });

    console.log(searchKeys);

    let images = await Image.findAll({
        where: {
            [Op.or]: searchKeys
        }
    });
    if(!images.length) {
        return res.status(400).send({"success": false, "error": "no images found", "images": []});
    }
    images.forEach(element => {
        let likeList = element.dataValues.likeList;
        element.dataValues.currentUserLikes = false;
        if(likeList.includes(parseInt(req.body.userId))) {
            element.dataValues.currentUserLikes = true;
        }
    });

    return res.send({
        "success": true,
        "images": images.slice(parseInt(req.body.index), parseInt(req.body.index) + parseInt(req.body.length))
    });
});

router.post('/share', async (req,res) => {
    console.log(req.body.userId, req.body.authorId, req.body.imageId);

    let portfolios = await Portfolio.findAll({
        where: {
            [Op.and]: [
                {UserId: req.body.userId},
                {authorId: req.body.authorId}
            ]
        }
    });

    if(!portfolios.length) {
        await Portfolio.create({
            UserId: req.body.userId, // person to be shared with
            authorId: req.body.authorId, // original person who uploaded
            portfolio: [req.body.imageId]
        });
    }
    else {
        let portfolio = portfolios[0].dataValues.portfolio;
        if(!portfolio.includes(req.body.imageId))
            portfolio.push(req.body.imageId);
        else 
            return res.send({"success": false, "error": "Image has already been shared"});

        await Portfolio.update({
            portfolio: portfolio,
        }, {
            where: {
                [Op.and]: [
                    {UserId: req.body.userId},
                    {authorId: req.body.authorId}
                ]
            }
        });
    }

    return res.send({"success": true});
});

module.exports = router;
