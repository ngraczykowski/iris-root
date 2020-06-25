const express = require('express');
const app = express();
const bodyParser = require("body-parser");
const dataFolder = './data';
const fs = require('fs');
const cors = require('cors');
const path = require('path');

app.use(bodyParser.json());
app.use(cors());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.get('/rest/webapp/api/users', (req, res) => {
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

app.post('/rest/webapp/api/users', (req, res) => {
  let dataFile;

  try {
    dataFile = fs.readFileSync(`${dataFolder}/users.json`);
    let json = JSON.parse(dataFile);

    if (isUserNameUnique(req.body.userName, json.content) && !hasForbiddenCharacters(req.body.userName)) {
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
    } else if(hasForbiddenCharacters(req.body.userName)) {
      res.status(422).send();
    } else {
      res.status(409).send();
    }

  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.get('/rest/webapp/api/users/roles', (req, res) => {
  let dataFile;

  try {
    dataFile = fs.readFileSync(`${dataFolder}/roles.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.patch('/rest/webapp/api/users/:userName', (req, res) => {
  dataFile = fs.readFileSync(`${dataFolder}/users.json`);
  let json = JSON.parse(dataFile);
  const userObject = getUserObjectByUserName(req.params.userName, json.content);

  if (userObject.index !== null && userObject.user !== null) {
    json.content[userObject.index].displayName = req.body.displayName;
    json.content[userObject.index].roles = req.body.roles;
    try {
      fs.writeFileSync(`${dataFolder}/users.json`, JSON.stringify(json));
      res.status(204).send();
    } catch (err) {
      if (err.code === 'ENOENT') {
        res.status(404).send()
      }
    }
  } else {
    res.status(507).send(); //insufficientStorage
    // res.status(422).send(); //entityProcessingError
  }
});

app.patch('/rest/webapp/api/users/:userName/password/reset', (req, res) => {
  dataFile = fs.readFileSync(`${dataFolder}/users.json`);
  let json = JSON.parse(dataFile);
  const userObject = getUserObjectByUserName(req.params.userName, json.content);

  if (userObject.index !== null && userObject.user !== null) {
    res.status(200).send(JSON.stringify(
      {
        "temporaryPassword": "Q2fqvnHW"
      }
    ));
  } else {
    res.status(404).send();
  }
});

app.get('/rest/webapp/api/decision-trees/:treeId/branches/:branchId', (req, res) => {
  let dataFile;
  const id = req.params.branchId;
  try {
    dataFile = fs.readFileSync(`${dataFolder}/reasoning-branch/${id}.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});


app.get('/rest/webapp/management/info', (req, res) => {
  let dataFile;
  try {
    dataFile = fs.readFileSync(`${dataFolder}/management_info.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.patch('/rest/webapp/api/decision-trees/:treeId/branches', (req, res) => {
  let dataFile;
  const id = req.body.branchIds[0];
  try {
    dataFile = fs.readFileSync(`${dataFolder}/reasoning-branch/${id}.json`);
    let json = JSON.parse(dataFile);
    json.active = req.body.active;
    json.aiSolution = req.body.aiSolution;
    fs.writeFileSync(`${dataFolder}/reasoning-branch/${id}.json`, JSON.stringify(json));
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.get('/rest/webapp/api/change-requests/pending', (req, res) => {
  let dataFile;
  try {
    dataFile = fs.readFileSync(`${dataFolder}/pending-changes.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.get('/rest/webapp/api/discrepant-branches', (req, res) => {
  let dataFile;
  try {
    dataFile = fs.readFileSync(`${dataFolder}/list-discrepant-branches.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.get('/rest/webapp/api/discrepant-branches/:id/discrepancy-ids', (req, res) => {
  let dataFile;
  const rbId = req.params.id;

  try {
    dataFile = fs.readFileSync(`${dataFolder}/list-discrepancy-ids-${rbId}.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.get('/rest/webapp/api/discrepancies', (req, res) => {
  dataFile = fs.readFileSync(`${dataFolder}/list-discrepancies-by-ids.json`);
  let discrepancies = JSON.parse(dataFile);
  const ids = req.query.id;

  if (ids !== null) {
    res.status(200).send(getDiscrepancies(ids, discrepancies));
  } else {
    res.status(404).send();
  }
});

app.get('/rest/webapp/api/bulk-changes', (req, res) => {
  let dataFile;
  try {
    dataFile = fs.readFileSync(`${dataFolder}/bulk-changes.json`);
    res.status(200).send(JSON.parse(dataFile));
  } catch (err) {
    if (err.code === 'ENOENT') {
      res.status(404).send()
    }
  }
});

app.patch('/rest/webapp/api/change-request/:changeRequestId/approve', (req, res) => {
  res.status(200).send();
  console.log(`CR-${req.params.changeRequestId} - Approved`);
});

app.patch('/rest/webapp/api/change-request/:changeRequestId/reject', (req, res) => {
  res.status(200).send();
  console.log(`CR-${req.params.changeRequestId} - Rejected`);
});

app.patch('/rest/webapp/api/change-request/:changeRequestId/cancel', (req, res) => {
  res.status(200).send();
  console.log(`CR-${req.params.changeRequestId} - Cancelled`);
});

app.get('/rest/webapp/api/report/security-matrix-report', (req, res) => {
  res.setHeader('Content-Type', 'text/csv');
  res.sendFile(__dirname + '/data/security-matrix-report.csv');
});

app.get('/rest/webapp/api/report/reasoning-branch-report', (req, res) => {
  let filePath = path.join(__dirname, 'data/security-matrix-report.csv');
  let stat = fs.statSync(filePath);

  if (req.query.decisionTreeId === '1') {
    setTimeout(() => {
      res.writeHead(200, {
        "Content-Type": "text/csv",
        "Content-Disposition": "attachment; filename=report.csv",
        'Content-Length': stat.size
      });
      fs.createReadStream(filePath).pipe(res);
    }, 5000);
  } else {
    res.status(400).send();
  }
});

app.get('/rest/webapp/api/report/audit-report', (req, res) => {
  let filePath = path.join(__dirname, 'data/security-matrix-report.csv');
  let stat = fs.statSync(filePath);

  setTimeout(() => {
    res.writeHead(200, {
      "Content-Type": "text/csv",
      "Content-Disposition": "attachment; filename=report.csv",
      'Content-Length': stat.size
    });
    fs.createReadStream(filePath).pipe(res);
  }, 5000);
});

app.put('/rest/webapp/api/decision-trees/:treeId/branches/validate', (req, res) => {
  const { featureVectorSignatures = [], branchIds = [] } = req.body;

  if (typeof branchIds !== 'undefined' && branchIds.indexOf(666) != -1 || branchIds.indexOf(999) != -1) {
    res.status(400).send(JSON.stringify({
      "key": "Branch(es) not found",
      "date": 1590049124.408017,
      "exception": "com.silenteight.sens.webapp.backend.reasoningbranch.BranchIdsNotFoundException",
      "extras": {
          "branchIds": [
            123,
            456
          ]
      }
    }))
  } else if (typeof featureVectorSignatures !== 'undefined' && featureVectorSignatures.indexOf('G7stUa5Fyy3oOJZDpeZa+cHQ+Gg=') != -1) {
    res.status(400).send(JSON.stringify({
      "key": "Branch(es) not found",
      "date": 1590049200.271666,
      "exception": "com.silenteight.sens.webapp.backend.reasoningbranch.FeatureVectorSignaturesNotFoundException",
      "extras": {
        "featureVectorSignatures": [
          "G7stUa5Fyy3oOJZDpeZa+cHQ+Gg="
        ]
      }
    }))
  } else if (branchIds) {
    res.status(200).send(JSON.stringify({
      "branchIds": [
        {
          "branchId": 1,
          "featureVectorSignature": "G7stUa5Fyy3oOJZDpeZa+cHQ+Gg="
        },
        {
          "branchId": 2,
          "featureVectorSignature": "Nvr8IkZNgs8cFG3IDXMzhIw0sD4"
        }
      ]
    }))
  } else if (featureVectorSignatures) {
    res.status(200).send(JSON.stringify({
      "branchIds": [
        {
          "branchId": 1,
          "featureVectorSignature": "G7stUa5Fyy3oOJZDpeZa+cHQ+Gg="
        },
        {
          "branchId": 2,
          "featureVectorSignature": "Nvr8IkZNgs8cFG3IDXMzhIw0sD4"
        }
      ]
    }))
  }
});

app.post('/rest/webapp/api/bulk-changes', (req, res) => {
  res.status(200).send();
});

app.post('/rest/webapp/api/change-requests/pending', (req, res) => {
  res.status(200).send();
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

function getUserObjectByUserName(userName, userList) {
  let userObject = {
    index: null,
    user: null
  };

  userObject.user = userList.filter((user, index) => {
    if (user.userName === userName) {
      userObject.index = index;
      return user;
    }
  });

  return userObject;
}

function getDiscrepancies(ids, discrepancies) {
  ids = ids.split(',').map(Number);

  let discrepanciesList = [];

  ids.forEach(id => {
    discrepancies.filter((element) => {
      if (element.id === id) {
        discrepanciesList.push(element);
      }
    })
  })

  return discrepanciesList;
}

app.listen(24410, () => console.log('REST API mock server started at http://localhost:24410/'));
module.exports = app;
