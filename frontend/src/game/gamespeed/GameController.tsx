import * as React from 'react';
import styled from 'styled-components';
import { Row } from '../../common/ui/Row';
import Slider from '../../common/ui/Slider';
import Config from '../../Config';
import { PlayControllButton } from './PlayControllButton';

interface Props {
  gameSpeedChange?: (value: number) => void;
  gameSpeedPause?: () => void;
  restartGame?: () => void;
  width?: string;
}

interface State {
  playing: boolean;
}

export class GameController extends React.Component<Props, State> {
  private sliderRef: Slider | null;

  constructor(props: Props) {
    super(props);
    this.state = {
      playing: true,
    };
    this.playOrPause = this.playOrPause.bind(this);
    this.gameSpeedChange = this.gameSpeedChange.bind(this);
  }

  public render() {
    const playing = this.state.playing;
    return (
      <Row>
        <PlayControllButton onClick={this.playOrPause} playing={playing} />
        <GameSpeedContainer>
          <div>Game Speed</div>
          <Slider
            ref={x => (this.sliderRef = x)}
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

  private gameSpeedChange() {
    const { playing } = this.state;
    const { gameSpeedChange } = this.props;
    if (playing && this.sliderRef && gameSpeedChange) {
      const currentGameSpeed = this.sliderRef.currentValue();
      gameSpeedChange(currentGameSpeed);
    }
  }

  private setPlayStatus(playing: boolean) {
    this.setState({
      playing,
    });
  }

  private playOrPause() {
    const { gameSpeedPause, gameSpeedChange } = this.props;
    const { playing } = this.state;
    if (this.sliderRef && gameSpeedPause && gameSpeedChange) {
      const currentGameSpeed = this.sliderRef.currentValue();
      if (playing) {
        gameSpeedPause();
        this.setPlayStatus(false);
      } else {
        gameSpeedChange(currentGameSpeed);
        this.setPlayStatus(true);
      }
    }
  }
}

const GameSpeedContainer = styled.div`
  padding-left: 20px;
`;
