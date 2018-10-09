import axios, { AxiosResponse } from 'axios';
import * as React from 'react';
import GameBoardContainer from './board/GameBoardContainer';
import {ICharacterInfo, ICoordinate, IGameMap, IGameState, IPlayer, ITile, TileType } from './game.typings';

interface IState {
    tiles: Map<ICoordinate, ITile>
    width: number,
    height: number
}

const colours = ['#4286f4', '#d3422c', '#88d852', '#f0fc0c', '#7c0cb5']

export default class extends React.Component<any, IState> {
    public tiles = new Map<ICoordinate, ITile>();

    constructor(props: any) {
        super(props);
    }

    public handleChange(gameState: IGameState) {
        const newPlayers = this.createPlayers(gameState);
        this.createTiles(gameState, newPlayers);

        this.setState({ tiles: this.tiles, width: gameState.map.width, height: gameState.map.height });
    }

    public componentDidMount() {
        axios.get('http://localhost:8080/state')
            .then((response: AxiosResponse<IGameState>) => {
                this.handleChange(response.data);
            });
    }

    public render() {
        if (this.state && this.state.tiles) {
            return(
                <GameBoardContainer tiles={this.state.tiles} width={this.state.width} height={this.state.height} />
            );
        }
        return null;
    }

    private createPlayers(gameState: IGameState): Map<string, IPlayer> {
        const playersMap = new Map<string, IPlayer>();
        gameState.map.characterInfos.forEach((characterInfo, index) => {
            const player = {
                colour: colours[index],
                id: characterInfo.id,
                name: characterInfo.name,
                points: characterInfo.points,
            }
            playersMap.set(player.id, player);
        });
        return playersMap;
    }

    private createTiles(gameState: IGameState, players: Map<string, IPlayer>) {
        const map = gameState.map; 
        const characterInfos = map.characterInfos as ICharacterInfo[];
        characterInfos.forEach(c => { this.mapCharacterInfoToTile(c, map, players) });
    }

    private mapCharacterInfoToTile(c: ICharacterInfo, map: IGameMap, players: Map<string, IPlayer>) {
        this.setPlayerTile(c.position, c.id, map, players);
        this.setColouredTiles(c.colouredPositions, c.id, map, players);
    }

    private setColouredTiles(colouredPositions: number[], playerId: string, map: IGameMap, players: Map<string, IPlayer>): void {
        colouredPositions.forEach(colouredPosition => {
            const colouredTile = {} as ITile;
            colouredTile.coordinate = this.getCoordinateFromMapPosition(colouredPosition, map)
            const player = players.get(playerId);
            colouredTile.color = player? player.colour: undefined;
            this.tiles.set(colouredTile.coordinate, colouredTile);
        });
    }

    private setPlayerTile(position: number, playerId: string, map: IGameMap, players: Map<string, IPlayer>): void {
        const playerTile = {} as ITile
        playerTile.coordinate = this.getCoordinateFromMapPosition(position, map);
        const player = players.get(playerId);
        playerTile.colorOfInhabitingPlayer = player? player.colour: undefined;
        playerTile.type = TileType.CHARACTER;
        this.tiles.set(playerTile.coordinate, playerTile);
        
    }

    private getCoordinateFromMapPosition(position: number, map: IGameMap): ICoordinate {
        const yPosition = Math.floor(position / map.width);
        const xPosition = position - (yPosition * map.width);
        return { x: xPosition, y: yPosition } as ICoordinate;
    } 
}