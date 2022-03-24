var express = require('express');
var router = express.Router();
const passport = require('../library/passport/passport');
const Users = require('../library/model/users');
const bcryptjs = require('bcryptjs');
const crypto = require("crypto");
const { token } = require('morgan');

require('dotenv').config();

router.post('/signup', function(req, res, next) {
    console.log(req.body);
    bcryptjs.genSalt(parseInt(process.env.BCRYPT_SALT, 10), (err, salt) => {
        if (err)
            return res.status(400).json({'errors': 'hash errors'});

        bcryptjs.hash(req.body.password, salt, (err, hash) => {
            if (err) {
                console.log(err);
                return res.status(400).json({'errors': err, 'success': false});
            }

            Users.create({
                email: req.body.email,
                password: hash,
                firstName: req.body.firstName,
                lastName: req.body.lastName,
                portfolio: [],
                uploadCount: 0,
                likeCount: 0,
                token: crypto.randomBytes(16).toString("hex"),
            }).then(result => {
                Users.findAll({
                    where: {
                        email: req.body.email
                    }
                }).then(results => {
                    return res.status(200).json({
                        'id': results[0].id,
                        'firstName': results[0].firstName,
                        'lastName': results[0].lastName,
                        'email': results[0].email,
                        'portfolio': results[0].portfolio,
                        'uploadCount': results[0].uploadCount,
                        'likeCount': results[0].likeCount,
                        'token': results[0].token,
                        'success': true
                    });
                });
            }).catch(err => {
                return res.status(400).json({'error': 'user creation error', 'success': false});
            });
        });
    });
});

router.post('/signin', (req, res, next) => {
    
    passport.authenticate('local', (err, user, info) => {
        console.log('user check: ', user)
        if(err) {
            console.log(err)
            return res.status(400).json({'errors': 'authenticate error', 'success': false});
        }

        if(!user)
            return res.status(400).json({'errors': 'user not found', 'success': false});

        req.logIn(user, (err) => {
            console.log('in req')
            if(err)
                return res.status(400).json({'errors': 'failed login', 'success': false});

            return res.status(200).json({
                'id': user.id,
                'firstName': user.firstName,
                'lastName': user.lastName,
                'token': user.token,
                'success': true
            });
        });
    })(req, res, next);
});

module.exports = router;