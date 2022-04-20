const { DataTypes } = require('sequelize');
const sequelize = require('../../config/database');
const Users = require('./users');

const Portfolio = sequelize.define('Portfolio', {
  authorId: {
    type:DataTypes.INTEGER,
    allowNull: false
  },
  portfolio: {
    type: DataTypes.JSON,
    allowNull: false
  }
}, {
    // Other model options go here
});

Users.hasMany(Portfolio, {
  onDelete: 'CASCADE',
});
Portfolio.belongsTo(Users);

module.exports = Portfolio;
