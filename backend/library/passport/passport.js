const bcryptjs = require('bcryptjs');
const Users = require('../model/users');
const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;

passport.serializeUser((user, done) => {
    done(null, user.id);
});

passport.deserializeUser((id, done) => {
    Users.findAll({
        where: {
            id: id
        }
    }).then(results => {
        done(null, results[0]);
    }).catch(err => {
        done(err, null);
    });
});

passport.use(new LocalStrategy({usernameField: 'email'}, (email, password, done) =>  {
    Users.findAll({
        where: {
            email: email
        }
    }).then(result => {
        bcryptjs.compare(password, result[0].password, (err, res) => {
            if (err)
                return done(err, null);

            if (res === true)
                return done(null, result[0]);
            
            return done(false, null);
        });
    }).catch(err => {
        console.log('passport messed');
        console.log(err);
        return done(err, null);
    });
}));

module.exports = passport;