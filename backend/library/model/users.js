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
    },
    token: {
      type: DataTypes.STRING,
      allowNull: false
    },
    uploadCount: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    likeCount: {
      type: DataTypes.INTEGER,
      allowNull: false
    }
  }, {
    // Other model options go here
});

module.exports = Users;
