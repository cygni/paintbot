import * as React from 'react';
import styled from 'styled-components';
import Config from '../Config';
import * as background from '../resources/background.jpg';
import GameBoardContainer from './gameboard/GameBoardContainer';
import GameBoardFactory from './gameboard/GameBoardFactory';
import ScoreBoardContainer from './scoreboard/ScoreBoardContainer';
import TimerPane from './timer/TimerPane';
import { Game, GameMap, GameSettings } from './type';

interface Props {
  gameMap: GameMap;
  gameSettings: GameSettings;
}

interface State {
  game: Game;
}

export default class GameContainer extends React.Component<Props, State> {
  public timerPane: TimerPane;
  public readonly gameBoardFactory: GameBoardFactory;

  public constructor(props: Props) {
    super(props);
    this.gameBoardFactory = new GameBoardFactory();
  }

  public render() {
    const { gameSettings, gameMap } = this.props;
    const game = this.transformGameMapToModel(gameMap);
    return (
      <WindowContainer>
        <HeaderContainer>
          <GameNameContainer>XYZ-Bot</GameNameContainer>
          <TimerPane
            durationInSeconds={Config.TimerSeconds}
            timeInMsPerTick={gameSettings.timeInMsPerTick}
            worldTick={gameMap.worldTick}
            ref={instance => {
              if (instance !== null) {
                this.timerPane = instance;
              }
            }}
          />
        </HeaderContainer>
        <Container>
          <ScoreBoardContainer
            players={game.currentCharacters}
            worldTick={game.worldTick}
          />
          <GameBoardContainer game={game} />
        </Container>
      </WindowContainer>
    );
  }

  private transformGameMapToModel(gameMap: GameMap): Game {
    this.gameBoardFactory.updateGameMap(gameMap);

    const game = {
      tiles: this.gameBoardFactory.createTiles(),
      currentCharacters: this.gameBoardFactory.createCharacters(),
      previousCharacters: this.gameBoardFactory.getPreviousCharacters(),
      bombs: this.gameBoardFactory.createPowerUps(),
      worldTick: this.gameBoardFactory.getWorldTick(),
      width: this.gameBoardFactory.getWidth(),
      height: this.gameBoardFactory.getHeight(),
    };

    return game;
  }
}

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

const WindowContainer = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  text-align: center;
  color: white;
  background-image: url(${background});
  background-size: cover;
  background-repeat: no-repeat;
`;
