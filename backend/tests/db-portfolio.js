const Portfolio = require('../library/model/portfolio');

async function initial() {
    await Portfolio.sync({ force: true });
    console.log("The table for the User model was just (re)created!");

    Portfolio.create({
        UserId: 2, // person to be shared with
        authorId: 1, // original person who uploaded
        portfolio: [1,2]
    }).then(value => {
        Portfolio.findAll({
            where: {
                UserId: 2
            }
        }).then(results => {
            console.log("All users:", JSON.stringify(results, null, 2));
            console.log(results[0].authorId);
            console.log(results[0].portfolio);
        });
    }).catch(err => {
        console.log(err)
    });
}

initial().catch(err => {
    console.log(err);
});