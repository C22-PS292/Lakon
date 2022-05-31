const {
  addWayangHandler,
  getAllWayangsHandler,
  getWayangByIdHandler,
} = require('./handler');

const routes = [
  {
    method: 'POST',
    path: '/wayang',
    handler: addWayangHandler,
  },
  {
    method: 'GET',
    path: '/wayang',
    handler: getAllWayangsHandler,
  },
  {
    method: 'GET',
    path: '/wayang/{wayangId}',
    handler: getWayangByIdHandler,
  },
];

module.exports = routes;
