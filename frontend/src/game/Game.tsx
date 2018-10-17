// import axios, { AxiosResponse } from 'axios';
import * as React from 'react';
import GameBoardContainer from './board/GameBoardContainer';
import {EventType, ICharacterInfo, ICoordinate, IGameMap, IGameState, IPlayer, ITile, TileType } from './game.typings';

interface IState {
    tiles: Map<string, ITile>
    width: number,
    height: number
}

const colours = ['#4286f4', '#d3422c', '#88d852', '#f0fc0c', '#c774f2']

export default class extends React.Component<any, IState> {
    public tiles = new Map<string, ITile>();
    public players = new Map<string, IPlayer>();

    public render() {
        return this.state && this.state.tiles 
        ?
            <div id='container'>
            <h1>XYZ-BOT</h1>
                <GameBoardContainer tiles={this.state.tiles} width={this.state.width} height={this.state.height} tileWidth={23} tileHeight={21}/> 
            </div>
        :
            null;
    }

    public componentDidMount() {
        const ws = new WebSocket('ws://localhost:8999');
        ws.onmessage = (evt: MessageEvent) => this.handleChange(evt);
    }

    private handleChange(evt: MessageEvent) {
        const gameState = JSON.parse(evt.data) as unknown as IGameState;
        if(gameState.type === EventType.MAP_UPDATE_EVENT) {
            this.updateMap(gameState);
        }
        if(gameState.type === EventType.GAME_ENDED_EVENT) {
            this.endGame(gameState);
        }
    }

    private updateMap(gameState: IGameState) {
        const map = gameState.map; 
        const characterInfos = map.characterInfos as ICharacterInfo[];
        this.initializeTiles(map.width, map.height);
        this.createPlayers(map);
        this.createTiles(map, characterInfos);
        this.setState({ tiles: this.tiles, width: gameState.map.width, height: gameState.map.height });
    }

    private endGame(gameState: IGameState) {
        sessionStorage.clear();
    }

    private initializeTiles(width: number, height: number) {
        for(let i = 0; i < width; i++) {
            for(let j = 0; j < height; j++) {
                const c = { x: i, y: j } as ICoordinate;
                const tile = { coordinate: c, type: TileType.EMPTY } as ITile;
                this.tiles.set(JSON.stringify(c), tile);
            } 
        }
    }

    private createPlayers(map: IGameMap): void {
        map.characterInfos.forEach((characterInfo, index) => {
            const player = {
                colour: colours[index],
                id: characterInfo.id,
                name: characterInfo.name,
                points: characterInfo.points,
            }
            this.players.set(player.id, player);
        });
    }

    private createTiles(map: IGameMap, characterInfos: ICharacterInfo[]) {
        characterInfos.forEach(c => { this.mapCharacterInfoToTile(c, map) });
        this.createObstacleTiles(map);
        this.createBombTiles(map);
    }

    private mapCharacterInfoToTile(c: ICharacterInfo, map: IGameMap) {
        this.createColouredTilesForPlayer(c.colouredPositions, c.id, map);
        this.createPlayerTile(c.position, c.id, map);
    }

    private createPlayerTile(position: number, playerId: string, map: IGameMap): void {
        const playerTile = {} as ITile
        playerTile.coordinate = this.getCoordinateFromMapPosition(position, map);
        const player = this.players.get(playerId);
        playerTile.colourOfInhabitingPlayer = player? player.colour: '';
        playerTile.playerId = player ? player.id: '';
        playerTile.type = TileType.CHARACTER;
        this.tiles.set(JSON.stringify(playerTile.coordinate), playerTile);
        
    }

    private createColouredTilesForPlayer(colouredPositions: number[], playerId: string, map: IGameMap): void {
        colouredPositions.forEach(colouredPosition => {
            const colouredTile = {} as ITile;
            colouredTile.coordinate = this.getCoordinateFromMapPosition(colouredPosition, map)
            colouredTile.isColoured = true;
            colouredTile.type = TileType.EMPTY;
            const player = this.players.get(playerId);
            colouredTile.isInhabited = player !== undefined;
            colouredTile.colour = player ? player.colour: '';

            this.tiles.set(JSON.stringify(colouredTile.coordinate), colouredTile);
        });
    }

    private createBombTiles(map: IGameMap) {
        map.bombPositions.forEach(bombPosition => {
            const bombTile = {} as ITile
            bombTile.coordinate = this.getCoordinateFromMapPosition(bombPosition, map);
            bombTile.type = TileType.BOMB;

            this.tiles.set(JSON.stringify(bombTile.coordinate), bombTile);
        });
    }

    private createObstacleTiles(map: IGameMap) {
        map.obstaclePositions.forEach(bombPosition => {
            const obstacleTile = {} as ITile
            obstacleTile.coordinate = this.getCoordinateFromMapPosition(bombPosition, map);
            obstacleTile.type = TileType.OBSTACLE;

            this.tiles.set(JSON.stringify(obstacleTile.coordinate), obstacleTile);
        });
    }

    private getCoordinateFromMapPosition(position: number, map: IGameMap): ICoordinate {
        const yPosition = Math.floor(position / map.width);
        const xPosition = position - (yPosition * map.width);
        return { x: xPosition, y: yPosition } as ICoordinate;
    } 
}