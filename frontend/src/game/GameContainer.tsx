import React from 'react';
import styled from 'styled-components';

import { GameControllerColors } from '../common/Constants';
import { Row } from '../common/ui/Row';
import Config from '../Config';

import GameBoardContainer from './gameboard/GameBoardContainer';
import GameBoardFactory from './gameboard/GameBoardFactory';
import { GameController } from './gamespeed/GameController';
import ScoreBoardContainer from './scoreboard/ScoreBoardContainer';
import { TimerPane } from './timer/TimerPane';
import { Game, GameMap, GameSettings } from './type';

interface Props {
  gameMap: GameMap;
  gameSettings: GameSettings;
  gameSpeedChange: (changeValue: number) => void;
  gameSpeedPause: () => void;
  restartGame: () => void;
}

interface State {
  game: Game;
}

export default class GameContainer extends React.Component<Props, State> {
  private readonly gameBoardFactory = new GameBoardFactory();

  private transformGameMapToModel(gameMap: GameMap): Game {
    return this.gameBoardFactory.getGameBoard(gameMap);
  }

  render() {
    const { gameSettings, gameMap, gameSpeedChange, gameSpeedPause, restartGame } = this.props;
    const game = this.transformGameMapToModel(gameMap);
    return (
      <div>
        <HeaderContainer>
          <GameNameContainer>PAINTBOT</GameNameContainer>
          <TimerPane
            durationInSeconds={Config.TimerSeconds}
            timeInMsPerTick={gameSettings.timeInMsPerTick}
            worldTick={gameMap.worldTick}
          />
        </HeaderContainer>
        <Row justifyContent={'center'} style={{ paddingTop: 20 }}>
          <ScoreBoardContainer players={game.currentCharacters} worldTick={game.worldTick} />
          <div>
            <GameBoardContainer game={game} />
            <GamerControllerContainer>
              <GameController
                gameSpeedChange={gameSpeedChange}
                gameSpeedPause={gameSpeedPause}
                restartGame={restartGame}
              />
            </GamerControllerContainer>
          </div>
        </Row>
      </div>
    );
  }
}

const GamerControllerContainer = styled.div`
  padding: 5;
  backgroundcolor: ${GameControllerColors.Background};
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
