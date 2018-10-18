import * as React from 'react';
import { ICharacter } from '../game.typings';
import './ScoreBoard.css';
import ScoreBoardEntry from './ScoreBoardEntry';

interface IProps {
    players: Map<string, ICharacter>
}

export default class ScoreBoardContainer extends React.Component<IProps> {

    public getPlayers() {
        const players = Array.from(this.props.players.values());
        return players.map((player, index) => {
            return (
                <ScoreBoardEntry key={index} player={player} />
            );
        });
    } 

    public render() {
        return (
            <div className={'scoreboard'}>
                <h1>Scores</h1>
                {this.getPlayers()}
            </div>
        )
    }
}