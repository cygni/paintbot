import * as React from 'react';
import { Character } from '../type';
import './ScoreBoard.css';

interface Props {
  player: Character;
}

export default class ScoreBoardEntry extends React.Component<Props> {
  
  public shouldComponentUpdate(nextProps: Props) {
    return nextProps.player.points !== this.props.player.points;
  }

  public render() {
    const divStyle = { color: this.props.player.colour };
    return (
      <div className={'scoreboard-entry'} style={divStyle}>
        <b>
          {this.props.player.name} : {this.props.player.points}
        </b>
      </div>
    );
  }
}
