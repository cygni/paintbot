"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const http = require("http");
const WebSocket = require("ws");
const fs = require("fs");
const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });
wss.on('connection', async (ws) => {
    fs.readFile(`${__dirname}/complete-game.json`, 'utf8', async (err, data) => {
        console.log(err);
        const json = JSON.parse(data);
        for (let state of json.messages) {
            ws.send(JSON.stringify(state));
            console.log('Sent state: ', state);
            await sleep(500);
        }
    });
    ws.on('message', (message) => {
        console.log(`Received ${message}`);
        ws.send(`Hello, you sent -> ${message}`);
    });
});
const port = process.env.port || 8999;
server.listen(port, () => {
    console.log(`Server started on port ${port}`);
});
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
//# sourceMappingURL=server.js.map