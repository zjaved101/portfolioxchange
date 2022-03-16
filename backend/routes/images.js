var express = require('express');
var router = express.Router();
const Image = require('../library/model/images')
const User = require('../library/model/users')
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
        tags: JSON.parse(req.body.tags),
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
})

module.exports = router;
