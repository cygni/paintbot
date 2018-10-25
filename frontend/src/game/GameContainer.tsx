import * as React from 'react';
import styled from 'styled-components';
import { StandardColors, TileColors } from '../common/Constants';
import Config from '../Config';
import GameBoardContainer from './gameboard/GameBoardContainer';
import ScoreBoardContainer from './scoreboard/ScoreBoardContainer';
import Timer from './timer/Timer';
import {
  Character,
  CharacterInfo,
  Coordinate,
  EventType,
  GameMap,
  GameState,
  PowerUp,
  Tile,
  TileType,
} from './type';

interface Props {}

interface State {
  tiles: Map<string, Tile>;
  currentCharacters: Character[];
  previousCharacters: Character[];
  bombs: PowerUp[];
  worldTick: number;
}

const colours = ['#4286f4', '#d3422c', '#88d852', '#f0fc0c', '#c774f2'];
const WINDOW_WIDTH = window.innerWidth; // Tile size is adapted to size of window when app is loaded

const Container = styled.div`
  display: inline-flex;
  padding-top: 20px;
  margin: auto;
`;

const HeaderContainer = styled.div`
  position: relative;
  display: flex;
  padding: 10px;
  font-size: 40px;
  justify-content: center;
  flex-direction: row;
`;

const GameNameContainer = styled.div`
  position: absolute;
  left: 0;
`;

const TimerContainer = styled.div`
  display: flex;
`;

export default class GameContainer extends React.Component<Props, State> {
  private map: GameMap;
  private timer: Timer;
  private readonly tiles: Map<string, Tile>;
  private readonly ws: WebSocket;

  public constructor(props: Props) {
    super(props);
    this.ws = new WebSocket(Config.WebSocketApiUrl);
    this.tiles = new Map<string, Tile>();
    this.map = {
      bombPositions: [],
      width: 46,
      height: 34,
      characterInfos: [],
      obstaclePositions: [],
      worldTick: 0,
    };
    this.state = {
      tiles: new Map<string, Tile>(),
      currentCharacters: [],
      previousCharacters: [],
      bombs: [],
      worldTick: this.map.worldTick,
    };
  }

  public render() {
    return this.state && this.state.tiles ? (
      <div>
        <HeaderContainer>
          <GameNameContainer>XYZ-Bot</GameNameContainer>
          <TimerContainer>
            <Timer
              startTimeInMinutes={Config.TimerMinutes}
              ref={x => {
                if (x !== null) {
                  this.timer = x;
                }
              }}
            />
          </TimerContainer>
        </HeaderContainer>
        <Container>
          {this.tryRenderScoreBoard()}
          {this.tryRenderGameBoard()}
        </Container>
      </div>
    ) : null;
  }

  public componentDidMount() {
    this.timer.start();
    this.ws.onmessage = (evt: MessageEvent) => this.onUpdateFromServer(evt);
  }

  public componentWillUnmount() {
    this.endGame();
  }

  private onUpdateFromServer(evt: MessageEvent) {
    const gameState = (JSON.parse(evt.data) as unknown) as GameState;
    if (gameState.type === EventType.MAP_UPDATE_EVENT) {
      this.updateMap(gameState);
    }
    if (gameState.type === EventType.GAME_ENDED_EVENT) {
      this.endGame();
    }
  }

  private tryRenderGameBoard() {
    const TILE_SIZE = this.map ? WINDOW_WIDTH / this.map.width / 1.7 : 0;
    return this.state.tiles.size > 0 ? (
      <GameBoardContainer
        tiles={this.state.tiles}
        characters={this.state.currentCharacters}
        previousCharacters={this.state.previousCharacters}
        bombs={this.state.bombs}
        width={this.map.width}
        height={this.map.height}
        tileWidth={TILE_SIZE}
        tileHeight={TILE_SIZE}
      />
    ) : (
      'Game is loading'
    );
  }

  private tryRenderScoreBoard() {
    return this.state.currentCharacters.length > 0 ? (
      <ScoreBoardContainer
        players={this.state.currentCharacters}
        worldTick={this.state.worldTick}
      />
    ) : (
      undefined
    );
  }

  private updateMap(gameState: GameState) {
    this.map = gameState.map;

    this.addObstacleTiles(this.map.obstaclePositions, this.tiles);
    this.addColouredTilesForPlayers(this.map.characterInfos, this.tiles);

    this.setState(previousState => {
      return {
        tiles: this.tiles,
        currentCharacters: this.createCharacters(this.map.characterInfos),
        previousCharacters: previousState.currentCharacters,
        bombs: this.createBombs(this.map.bombPositions),
        worldTick: this.map.worldTick,
      };
    });
  }

  private endGame() {
    this.timer.stop();
    this.ws.close();
  }

  private addColouredTilesForPlayers(
    characterInfos: CharacterInfo[],
    tiles: Map<string, Tile>,
  ): void {
    characterInfos.forEach(character => {
      this.createColouredTilesForPlayer(character, tiles);
    });
  }

  private createColouredTilesForPlayer(
    character: CharacterInfo,
    tiles: Map<string, Tile>,
  ): void {
    character.colouredPositions.forEach(colouredPosition => {
      const colouredTile = {} as Tile;
      colouredTile.coordinate = this.getCoordinateFromMapPosition(
        colouredPosition,
      );

      colouredTile.type = TileType.COLOURED;
      const theCharacter = this.state.currentCharacters.filter(
        c => c.id === character.id,
      )[0];
      colouredTile.colour = theCharacter
        ? theCharacter.colour
        : StandardColors.White;

      tiles.set(JSON.stringify(colouredTile.coordinate), colouredTile);
    });
  }

  private createBombs(bombPositions: number[]): PowerUp[] {
    const bombs: PowerUp[] = [];
    bombPositions.forEach(bombPosition => {
      const bomb = {} as PowerUp;
      bomb.coordinate = this.getCoordinateFromMapPosition(bombPosition);
      bomb.image = '/images/bomb.png';
      bombs.push(bomb);
    });
    return bombs;
  }

  private addObstacleTiles(
    obstaclePositions: number[],
    tiles: Map<string, Tile>,
  ) {
    obstaclePositions.forEach(bombPosition => {
      const obstacleTile = {} as Tile;
      obstacleTile.coordinate = this.getCoordinateFromMapPosition(bombPosition);
      obstacleTile.type = TileType.OBSTACLE;
      obstacleTile.colour = TileColors.Obstacle;
      tiles.set(JSON.stringify(obstacleTile.coordinate), obstacleTile);
    });
  }

  private createCharacters(characterInfos: CharacterInfo[]): Character[] {
    const characters: Character[] = [];
    characterInfos.forEach((characterInfo, index) => {
      const character = {
        colour: colours[index],
        id: characterInfo.id,
        name: characterInfo.name,
        points: characterInfo.points,
        coordinate: this.getCoordinateFromMapPosition(characterInfo.position),
      };
      characters.push(character);
    });
    return characters;
  }

  private getCoordinateFromMapPosition(position: number): Coordinate {
    const yPosition = Math.floor(position / this.map.width);
    const xPosition = position - yPosition * this.map.width;
    return { x: xPosition, y: yPosition } as Coordinate;
  }
}
