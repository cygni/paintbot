import * as React from 'react';
import GameBoardContainer from './board/GameBoardContainer';
import { IGameState } from './game.typings';

interface IState {
    gameState: IGameState
}

export default class extends React.Component<any, IState> {

    public map: IGameMap;
    public tiles = new Map<string, ITile>();
    public currentCharacters = new Map<string, ICharacter>();
    public previousCharacters = new Map<string, ICharacter>();
    public bombs: IBomb[] = [];
    public ws: WebSocket;

    public render() {
        return this.state && this.state.tiles 
        ?
            <div id='container'>
                <h1>XYZ-BOT</h1>
                <GameBoardContainer 
                    tiles={this.state.tiles} 
                    characters={this.state.currentCharacters} 
                    previousCharacters={this.state.previousCharacters} 
                    bombs={this.state.bombs} width={this.map.width} 
                    height={this.map.height} tileWidth={TILE_WIDTH} 
                    tileHeight={TILE_HEIGHT}
                /> 
            </div>
        :
            null;
    }

    public componentDidMount() {
        this.ws = new WebSocket('ws://localhost:8999');
        this.ws.onmessage = (evt: MessageEvent) => this.onUpdateFromServer(evt);
    }

    private onUpdateFromServer(evt: MessageEvent) {
        const gameState = JSON.parse(evt.data) as unknown as IGameState;
        if(gameState.type === EventType.MAP_UPDATE_EVENT) {
            this.updateMap(gameState);
        }
        if(gameState.type === EventType.GAME_ENDED_EVENT) {
            this.endGame(gameState);
        }
    }

    private updateMap(gameState: IGameState) {
        this.map = gameState.map; 
        this.addEmptyTiles(this.map.width, this.map.height);
        this.addObstacleTiles(this.map.obstaclePositions);
        // Save the previous characters and their positions to enable animation from previous position to next
        this.previousCharacters = new Map(this.currentCharacters);
        this.addCharacters(this.map.characterInfos);
        this.addColouredTilesForPlayers(this.map.characterInfos);
        this.addBombs(this.map.bombPositions);
        this.setState({ 
            tiles: this.tiles, 
            currentCharacters: this.currentCharacters, 
            previousCharacters: this.previousCharacters, 
            bombs: this.bombs, 
        });
    }

    private endGame(gameState: IGameState) {
        this.tiles.clear();
        this.currentCharacters.clear();
        this.previousCharacters.clear();
        this.map = {} as IGameMap;
        this.bombs = [];
        this.ws.close();
        
    }

    private addColouredTilesForPlayers(characterInfos: ICharacterInfo []) {
        characterInfos.forEach(c => { 
            this.addColouredTilesForPlayer(c.colouredPositions, c.id);
        });
    }

    private addColouredTilesForPlayer(colouredPositions: number[], playerId: string): void {
        colouredPositions.forEach(colouredPosition => {
            const colouredTile = {} as ITile;
            colouredTile.coordinate = this.getCoordinateFromMapPosition(colouredPosition)
            colouredTile.type = TileType.COLOURED;
            const player = this.currentCharacters.get(playerId);
            colouredTile.colour = player ? player.colour: 'white';

            this.tiles.set(JSON.stringify(colouredTile.coordinate), colouredTile);
        });
    }

    private addBombs(bombPositions: number[]) {
        bombPositions.forEach(bombPosition => {
            const bomb = {} as IBomb
            bomb.coordinate = this.getCoordinateFromMapPosition(bombPosition);
            bomb.image = 'resources/bomb.png';

            this.bombs.push(bomb);
        });
    }

    // TODO Fetch Gamestate from websocket server

    public render() {
        return(
            <GameBoardContainer gameState={this.state.gameState} />
        );
    }
}