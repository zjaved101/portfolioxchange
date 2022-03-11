const { DataTypes } = require('sequelize');
const sequelize = require('../../config/database');

const Users = sequelize.define('Users', {
    // Model attributes are defined here
    email: {
        type: DataTypes.STRING,
        unique: true
    },
    password: {
        type: DataTypes.STRING
    },
    firstName: {
      type: DataTypes.STRING,
    },
    lastName: {
      type: DataTypes.STRING
      // allowNull defaults to true
    },
    portfolio: {
      type: DataTypes.JSON,
      allowNull: false
    }
  }, {
    // Other model options go here
});

module.exports = Users;
