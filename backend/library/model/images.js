const { DataTypes } = require('sequelize');
const sequelize = require('../../config/database');
const Users = require('./users');

const Images = sequelize.define('Images', {
  title: {
    type: DataTypes.STRING,
    allowNull: false
  },
  description: {
    type: DataTypes.STRING,
    allowNull: false
  },
  imgType: {
    type: DataTypes.STRING,
    allowNull: false
  },
  tags: {
    type: DataTypes.JSON,
    allowNull: false
  },
  imgLoc: {
    type: DataTypes.STRING
  },
  likes: {
    type: DataTypes.INTEGER,
  },
  likeList: {
    type: DataTypes.JSON,
    allowNull: false
  }
}, {
    // Other model options go here
});

Users.hasMany(Images, {
  onDelete: 'CASCADE',
});
Images.belongsTo(Users);

module.exports = Images;
