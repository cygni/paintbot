import React from 'react';
import styled from 'styled-components';

import background from '../resources/background.jpg';

import GameDirector from './GameDirector';

const WindowContainer = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  text-align: center;
  color: white;
  background-image: url(${background});
  background-size: cover;
  background-repeat: no-repeat;
`;

export default function GameScreen() {
  return (
    <WindowContainer>
      <GameDirector />
    </WindowContainer>
  );
}
