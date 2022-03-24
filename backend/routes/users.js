var express = require('express');
var router = express.Router();
const Users = require('../library/model/users');

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.get('/profile', async (req, res) => {
  let users = await Users.findAll({
    where: {
        id: req.query.userId
    }
  });

  if(!users.length) {
        return res.status(400).send({"success": false, "error": "user id does not exist"});
  }
  // return res.send({"success": true, "user": users[0]});
  return res.status(200).json({
    'id': users[0].id,
    'firstName': users[0].firstName,
    'lastName': users[0].lastName,
    'email': users[0].email,
    'portfolio': users[0].portfolio,
    'uploadCount': users[0].uploadCount,
    'likeCount': users[0].likeCount,
    'token': users[0].token,
    'success': true
  });
  
});

module.exports = router;
