import * as React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { WelcomeScreenColors } from 'src/common/Constants';
import styled from 'styled-components';

const Container = styled.div`
        background-color: ${WelcomeScreenColors.Background}
    `;    


export default class Welcome extends React.Component<RouteComponentProps> {
    
    public render() {
        return(
            <Container>
                <h1>Welcome</h1>
                <button onClick={this.startGame}>Start Game</button>
            </Container>
        );
    }

    private startGame = () => {
        this.props.history.push('/game');
    }
}  