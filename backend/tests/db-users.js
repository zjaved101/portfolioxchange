const Users = require('../library/model/users');
const bcryptjs = require('bcryptjs');
const crypto = require("crypto");

async function initial() {
    await Users.sync({ force: true });
    console.log("The table for the User model was just (re)created!");

    bcryptjs.genSalt(parseInt(process.env.BCRYPT_SALT, 10), (err, salt) => {
        bcryptjs.hash('1234', salt, (err, hash) => {
            Users.create({
                email: 'john.doe@email.com',
                password: hash,
                firstName: 'John',
                lastName: 'Doe',
                portfolio: [],
                token: crypto.randomBytes(16).toString("hex"),
            }).then(value => {
                Users.findAll({
                    where: {
                        email: 'john.doe@email.com'
                    }
                }).then(results => {
                    console.log("All users:", JSON.stringify(results, null, 2));
                    console.log(results[0].id);
                    console.log(results[0].email);
                    console.log(results[0].token);
                });
            }).catch(err => {
                console.log(err)
            });
        });
    });
}

initial().catch(err => {
    console.log(err);
});