import * as React from 'react';
import { ICharacter } from '../game.typings';
import './ScoreBoard.css';

interface IProps {
    player: ICharacter
}
export default class ScoreBoardEntry extends React.Component<IProps> {

    public render() {
        const divStyle = { color: this.props.player.colour };
        return (
            <div className={'scoreboard-entry'} style={divStyle}>
                <b>{this.props.player.name} : {this.props.player.points}</b>
            </div>
        );
    }
}