import * as React from 'react';
import FlipMove from 'react-flip-move';
import styled from 'styled-components';
import { Header } from '../../common/Header';
import { Indent } from '../../common/Indent';
import { Character } from '../type';
import ScoreBoardEntry from './ScoreBoardEntry';
import { SortOrder, sortPlayers } from './Util';

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
        <Indent>
          <FlipMove>{this.getPlayers()}</FlipMove>
        </Indent>
        gi{' '}
      </Container>
    );
  }

  private getPlayers() {
    const playerArray = Array.from(this.props.players.values());
    const players = sortPlayers(playerArray, SortOrder.DESCENDING);
    return players.map((player, index) => {
      return <ScoreBoardEntry key={player.id} player={player} />;
    });
  }
}
