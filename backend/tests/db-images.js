const Images = require('../library/model/images');

async function initial() {
    await Images.sync({ force: true });
    console.log("The table for the User model was just (re)created!");

    Images.create({
        imgLoc: 'https://www.theedadvocate.org/wp-content/uploads/2016/02/board-361516_960_720.jpg',
        likes: 0,
        UserId: 1,
        title: 'Test',
        description: 'test',
        tags: ['nature'],
        imgType: 'jpg',
        likeList: []
    }).then(value => {
        Images.findAll({
            where: {
                id: 1
            }
        }).then(results => {
            console.log("All users:", JSON.stringify(results, null, 2));
            console.log(results[0].id);
            console.log(results[0].imgLoc);
        });
    }).catch(err => {
        console.log(err)
    });
}

initial().catch(err => {
    console.log(err);
});