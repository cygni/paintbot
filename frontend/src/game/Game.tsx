import axios, { AxiosResponse } from 'axios';
import * as React from 'react';
import GameBoardContainer from './board/GameBoardContainer';
import { IGameState } from './game.typings';

interface IState {
    gameState: IGameState
}

export default class extends React.Component<any, IState> {

    constructor(props: any) {
        super(props);

        this.state = {
            gameState: {} as IGameState
        }
    }

    public componentDidMount() {
        console.log('component mounted');
        axios.get('http://localhost:8080/state')
            .then((response: AxiosResponse<IGameState>) => {
                console.log(response);
                this.setState({ gameState: response.data });
                console.log(this.state.gameState);
            });
    }

    // TODO Fetch Gamestate from websocket server

    public render() {
        return(
            <GameBoardContainer gameState={this.state.gameState} />
        );
    }
}