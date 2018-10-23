import * as React from 'react';
import styled from 'styled-components';
import { Header } from '../../common/Header';
import { Character } from '../type';
import ScoreBoardEntry from './ScoreBoardEntry';

interface Props {
  players: Map<string, Character>;
}

const Container = styled.div`
  text-align: center;
  background-color: rgba(88, 98, 112, 0.75);
  margin-left: 10px;
  width: 300px;
`;

export default class ScoreBoardContainer extends React.Component<Props> {
  public render() {
    return (
      <Container>
        <Header label={'Score'} />
        {this.getPlayers()}
      </Container>
    );
  }

  private getPlayers() {
    const players = Array.from(this.props.players.values());
    return players.map((player, index) => {
      return <ScoreBoardEntry key={index} player={player} />;
    });
  }
}
