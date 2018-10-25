import * as React from 'react';
import styled from 'styled-components';
import Config from '../Config';
import GameBoardContainer from './gameboard/GameBoardContainer';
import GameBoardFactory from './gameboard/GameBoardFactory';
import ScoreBoardContainer from './scoreboard/ScoreBoardContainer';
import Timer from './timer/Timer';
import { Character, EventType, GameState, PowerUp, Tile } from './type';

interface Props {}

interface State {
  tiles: Map<string, Tile>;
  currentCharacters: Character[];
  previousCharacters: Character[];
  bombs: PowerUp[];
  worldTick: number;
}

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
  private timer: Timer;
  private gameBoardFactory: GameBoardFactory;

  private readonly ws: WebSocket;

  public constructor(props: Props) {
    super(props);
    this.gameBoardFactory = new GameBoardFactory();
    this.ws = new WebSocket(Config.WebSocketApiUrl);
    this.state = {
      tiles: new Map<string, Tile>(),
      currentCharacters: [],
      previousCharacters: [],
      bombs: [],
      worldTick: this.gameBoardFactory.getWorldTick(),
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
    const TILE_SIZE = WINDOW_WIDTH / this.gameBoardFactory.getWidth() / 1.7;
    return this.state.tiles.size > 0 ? (
      <GameBoardContainer
        tiles={this.state.tiles}
        characters={this.state.currentCharacters}
        previousCharacters={this.state.previousCharacters}
        bombs={this.state.bombs}
        width={this.gameBoardFactory.getWidth()}
        height={this.gameBoardFactory.getHeight()}
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
    this.gameBoardFactory.updateGameMap(
      gameState.map,
      this.state.currentCharacters,
    );

    this.setState(previousState => {
      return {
        tiles: this.gameBoardFactory.createTiles(this.state.tiles),
        currentCharacters: this.gameBoardFactory.createCharacters(),
        previousCharacters: previousState.currentCharacters,
        bombs: this.gameBoardFactory.createPowerUps(),
        worldTick: this.gameBoardFactory.getWorldTick(),
      };
    });
  }

  private endGame() {
    this.timer.stop();
    this.ws.close();
  }
}
