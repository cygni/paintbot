import * as React from 'react';
import styled from 'styled-components';
import GameContainer from './game/GameContainer';
import * as background from './resources/background.jpg';

const Container = styled.div`
  position: absolute;
  top: 0; right: 0; bottom: 0; left: 0;
  text-align: center;
  color: white;
  background-image: url(${background});
  background-size: cover;
  background-repeat: no-repeat;
`;

class App extends React.Component {
  public render() {
    return (
      <Container>
        <GameContainer />
      </Container>
    );
  }
}

export default App;
