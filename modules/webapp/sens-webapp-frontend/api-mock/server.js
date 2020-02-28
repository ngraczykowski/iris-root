const express = require('express');
const app = express();
const bodyParser = require("body-parser");
const dataFolder = './data';
const fs = require('fs');
const cors = require('cors');

app.use(bodyParser.json())
app.use(cors());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.get('/api/users', (req, res) => {
  let dataFile;

  try {
    dataFile = fs.readFileSync(`${dataFolder}/users.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.post('/api/users', (req, res) => {
  let dataFile;

  try {
    dataFile = fs.readFileSync(`${dataFolder}/users.json`);
    let json = JSON.parse(dataFile);

    json.content.push({
      userName: req.body.userName,
      displayName: req.body.displayName,
      roles: req.body.roles,
      lastLoginAt: null,
      createdAt: "+51916-01-15T06:50:14+01:00",
      origin: "GNS"
    });
    
    fs.writeFileSync(`${dataFolder}/users.json`, JSON.stringify(json));
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.get('/api/users/roles', (req, res) => {
  let dataFile;

  try {
    dataFile = fs.readFileSync(`${dataFolder}/roles.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
})

function isUserNameUnique(userName, currentUsersList) {
  const isUnique = currentUsersList.filter((user) => {
    return user.userName == userName;
  });
  return isUnique.length === 0;
}

function hasForbiddenCharacters(userName) {
  return userName.indexOf('!') >= 0;
}

app.listen(7070, () => console.log('REST API mock server started at http://localhost:7070/'))
module.exports = app;