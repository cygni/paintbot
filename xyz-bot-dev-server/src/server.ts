import * as express from 'express';
import * as http from 'http';
import * as WebSocket from 'ws';
import states from './data';
import * as fs from 'fs';
import { SSL_OP_EPHEMERAL_RSA } from 'constants';

const app = express();

const server = http.createServer(app);

const wss = new WebSocket.Server({ server });

wss.on('connection', async (ws: WebSocket) => {

    fs.readFile(`${__dirname}/complete-game.json`, 'utf8', async (err: any, data: any) => {
        console.log(err);
        const json = JSON.parse(data);
         for(let state of json.messages) {
            ws.send(JSON.stringify(state));
            console.log('Sent state: ', state);
            await sleep(500);
        }
    })
    
    ws.on('message', (message: string) => {
        console.log(`Received ${message}`);

        ws.send(`Hello, you sent -> ${message}`);
    });
});

const port = process.env.port || 8999;

server.listen(port, () => {
    console.log(`Server started on port ${port}`);
});

function sleep(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
}