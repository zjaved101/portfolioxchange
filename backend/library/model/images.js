const { DataTypes } = require('sequelize');
const sequelize = require('../../config/database');
const Users = require('./users');

const Images = sequelize.define('Images', {
    // Model attributes are defined here
    imgLoc: {
        type: DataTypes.STRING
    },
    likes: {
      type: DataTypes.INTEGER,
    }
  }, {
    // Other model options go here
});

Users.hasMany(Images, {
  onDelete: 'CASCADE',
});
Images.belongsTo(Users);

module.exports = Images;
