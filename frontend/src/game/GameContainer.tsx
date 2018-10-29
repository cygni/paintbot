import * as React from 'react';
import styled from 'styled-components';
import Config from '../Config';
import GameBoardContainer from './gameboard/GameBoardContainer';
import GameBoardFactory from './gameboard/GameBoardFactory';
import { GameSpeed } from './gamespeed/GameSpeed';
import ScoreBoardContainer from './scoreboard/ScoreBoardContainer';
import { TimerPane } from './timer/TimerPane';
import { Game, GameMap, GameSettings } from './type';

interface Props {
  gameMap: GameMap;
  gameSettings: GameSettings;
  gameSpeedChange: (changeValue: number) => void;
}

interface State {
  game: Game;
}

export default class GameContainer extends React.Component<Props, State> {
  private readonly gameBoardFactory: GameBoardFactory;

  public constructor(props: Props) {
    super(props);
    this.gameBoardFactory = new GameBoardFactory();
  }

  public render() {
    const { gameSettings, gameMap, gameSpeedChange } = this.props;
    const game = this.transformGameMapToModel(gameMap);
    return (
      <div>
        <HeaderContainer>
          <GameNameContainer>XYZ-Bot</GameNameContainer>
          <TimerPane
            durationInSeconds={Config.TimerSeconds}
            timeInMsPerTick={gameSettings.timeInMsPerTick}
            worldTick={gameMap.worldTick}
          />
        </HeaderContainer>
        <Container>
          <ScoreBoardContainer
            players={game.currentCharacters}
            worldTick={game.worldTick}
          />
          <div>
            <GameBoardContainer game={game} />
            <GameSpeed gameSpeedChange={gameSpeedChange} />
          </div>
        </Container>
      </div>
    );
  }

  private transformGameMapToModel(gameMap: GameMap): Game {
    return this.gameBoardFactory.getGameBoard(gameMap);
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
