import * as React from 'react';
import Config from '../Config';
import GameContainer from './GameContainer';
import { EventType, GameSettings, GameState } from './type';

interface Props {}

interface State {
  gameSettings: GameSettings | undefined;
  gameState: GameState | undefined;
}

// TODO Handle receiving game settings when EventType === GAME_STARTING_EVENT
// TODO Handle receiving results when EventType === GAME_RESULT_EVENT

export default class GameDirector extends React.Component<Props, State> {
  private readonly ws: WebSocket;
  public constructor(props: Props) {
    super(props);
    this.ws = new WebSocket(Config.WebSocketApiUrl);
    this.ws.onmessage = (evt: MessageEvent) => this.onUpdateFromServer(evt);
    this.state = {
      gameSettings: undefined,
      gameState: undefined,
    };
  }

  public render() {
    return this.getComponentToRender();
  }

  public getComponentToRender() {
    if (this.state.gameSettings && this.state.gameState) {
      const gameStatus = this.state.gameState.type;
      const { gameState, gameSettings } = this.state;

      if (!gameStatus) {
        return <h1>Waiting for game</h1>;
      } else if (gameStatus === EventType.GAME_STARTING_EVENT) {
        return <h1>Game is starting</h1>;
      } else if (gameStatus === EventType.GAME_UPDATE_EVENT) {
        return (
          <GameContainer gameMap={gameState.map} gameSettings={gameSettings} />
        );
      } else if (gameStatus === EventType.GAME_ENDED_EVENT) {
        this.endGame();
        return <h1>Game finished</h1>;
      }
    }
    return null;
  }

  public componentWillUnmount() {
    this.endGame();
  }

  private onUpdateFromServer(evt: MessageEvent) {
    const data = JSON.parse(evt.data);
    if (data.type === EventType.GAME_STARTING_EVENT) {
      this.setState({ gameSettings: data.gameSettings as GameSettings });
    } else if (data.type === EventType.GAME_UPDATE_EVENT) {
      this.setState({ gameState: data as GameState });
    }
  }

  private endGame() {
    this.ws.close();
  }
}
