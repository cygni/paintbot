import * as React from 'react';
import { IGameState, ITile } from '../game.typings';
import GameBoard from './GameBoard';

interface IProps {
    gameState: IGameState
}

interface IState {
    tiles: ITile[]
}

export default class GameBoardContainer extends React.Component<IProps, IState> {
    constructor(props: any) {
        super(props);

        this.state = {
            tiles: [] as ITile[]
        }
    }

    // TODO Create tiles from GameState

    public render() {
        return(
            <GameBoard tiles={this.state.tiles}/>
        );
    }
}