import * as React from 'react';
import { Character } from '../type';
import './ScoreBoard.css';
import ScoreBoardEntry from './ScoreBoardEntry';

interface Props {
  players: Map<string, Character>;
}

export default class ScoreBoardContainer extends React.Component<Props> {
  public getPlayers() {
    const players = Array.from(this.props.players.values());
    return players.map((player, index) => {
      return <ScoreBoardEntry key={index} player={player} />;
    });
  }

  public render() {
    return (
      <div className={'scoreboard'}>
        <h1>Scores</h1>
        {this.getPlayers()}
      </div>
    );
  }
}
