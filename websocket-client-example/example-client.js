
const WebSocket = require('ws');
const _ = require('lodash')

const ws = new WebSocket('wss://server.paintbot.cygni.se/training');

const registerPlayerData = {
    "type": "se.cygni.paintbot.api.request.RegisterPlayer",
    "playerName": "The Simple Painter 735",
    "gameSettings": {
        "maxNoofPlayers": 5,
        "timeInMsPerTick": 250,
        "obstaclesEnabled": true,
        "powerUpsEnabled": true,
        "addPowerUpLikelihood": 15,
        "removePowerUpLikelihood": 5,
        "trainingGame": true,
        "pointsPerTileOwned": 1,
        "pointsPerCausedStun": 5,
        "noOfTicksInvulnerableAfterStun": 3,
        "minNoOfTicksStunned": 8,
        "maxNoOfTicksStunned": 10,
        "startObstacles": 30,
        "startPowerUps": 0,
        "gameDurationInSeconds": 2,
        "explosionRange": 4,
        "pointsPerTick": false
    },
    "receivingPlayerId": null,
    "timestamp": 1585667296058
}

const startGameData = {
    "type": "se.cygni.paintbot.api.request.StartGame",
    "receivingPlayerId": null,
    "timestamp": 1585667296903
}

const sendMoveData = (tick, gameId, receivingPlayerId) => ({
    "type": "se.cygni.paintbot.api.request.RegisterMove",
    "gameId": gameId,
    "gameTick": tick,
    "direction": _.sample(['RIGHT', 'LEFT', 'UP', 'DOWN', 'STAY', 'EXPLODE']),
    "receivingPlayerId": receivingPlayerId,
    "timestamp": 1585667297031
})


const timestamp = () => new Date().toISOString();

const send = data => {
    console.log(`${timestamp()} Sending: \t[${data}]`);
    ws.send(data);
}

const jsonSend = (data) => send(JSON.stringify(data));

const recieve = (data) => {
    console.log(`${timestamp()} Recieved: \t[${data}]`);
    const jsonData = JSON.parse(data);
    switch (jsonData.type) {
        case 'se.cygni.paintbot.api.response.PlayerRegistered':
            jsonSend(startGameData)
            break;
        case 'se.cygni.paintbot.api.event.MapUpdateEvent':
            jsonSend(sendMoveData(jsonData.gameTick, jsonData.gameId, jsonData.receivingPlayerId))
            break;
        case 'se.cygni.paintbot.api.event.GameEndedEvent':
            ws.close();

    }
};

ws.on('open', () => {
    console.log(`${timestamp()} connection opened`)
    jsonSend(registerPlayerData);
});

ws.on('message', recieve);

ws.on('close', () => {
    console.log(`${timestamp()} connection closed`)
    process.exit()
});

