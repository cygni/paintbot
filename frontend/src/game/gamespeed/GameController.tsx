import React from 'react';
import styled from 'styled-components';

import { Row } from '../../common/ui/Row';
import Slider from '../../common/ui/Slider';
import Config from '../../Config';

import { PlayControllButton } from './PlayControllButton';

const GameSpeedContainer = styled.div`
  padding-left: 20px;
`;

interface Props {
  width?: string;
  gameSpeedChange?(value: number): void;
  gameSpeedPause?(): void;
  restartGame?(): void;
}

interface State {
  playing: boolean;
}

export class GameController extends React.Component<Props, State> {
  private readonly sliderRef = React.createRef<Slider>();

  readonly state: State = {
    playing: true,
  };

  private readonly gameSpeedChange = () => {
    const { playing } = this.state;
    const { gameSpeedChange } = this.props;
    if (playing && this.sliderRef.current !== null && gameSpeedChange) {
      const currentGameSpeed = this.sliderRef.current.currentValue();
      gameSpeedChange(currentGameSpeed);
    }
  };

  private setPlayStatus(playing: boolean) {
    this.setState({
      playing,
    });
  }

  private readonly playOrPause = () => {
    const { gameSpeedPause, gameSpeedChange } = this.props;
    const { playing } = this.state;
    if (this.sliderRef.current !== null && gameSpeedPause && gameSpeedChange) {
      const currentGameSpeed = this.sliderRef.current.currentValue();
      if (playing) {
        gameSpeedPause();
        this.setPlayStatus(false);
      } else {
        gameSpeedChange(currentGameSpeed);
        this.setPlayStatus(true);
      }
    }
  };

  render() {
    const playing = this.state.playing;
    return (
      <Row>
        <PlayControllButton onClick={this.playOrPause} playing={playing} />
        <GameSpeedContainer>
          <div>Game Speed</div>
          <Slider
            ref={this.sliderRef}
            minValue={Config.GameSpeedMin}
            maxValue={Config.GameSpeedMax}
            defaultValue={Config.DefaultGameSpeed}
            reverse={true}
            sliderChange={this.gameSpeedChange}
          />
        </GameSpeedContainer>
      </Row>
    );
  }
}
