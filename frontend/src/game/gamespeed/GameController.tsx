import * as React from 'react';
import { Row } from '../../common/ui/Row';
import Slider from '../../common/ui/Slider';
import Config from '../../Config';

interface Props {
  gameSpeedChange?: (value: number) => void;
  gameSpeedPause?: () => void;
  restartGame?: () => void;
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
  }

  public render() {
    const playing = this.state.playing;
    const playStatusText = playing ? 'Pause' : 'Play';
    return (
      <Row>
        <button onClick={this.playOrPause}>{playStatusText}</button>
        <div>Game Speed:</div>
        <Slider
          ref={x => (this.sliderRef = x)}
          minValue={Config.GameSpeedMin}
          maxValue={Config.GameSpeedMax}
          defaultValue={Config.DefaultGameSpeed}
          reverse={true}
        />
      </Row>
    );
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
