import * as React from 'react';
import styled from 'styled-components';
import { ScoreBoardColors } from '../../common/Constants';
import { Header } from '../../common/Header';
import { Indent } from '../../common/Indent';
import { Character } from '../type';
import ScoreBoardEntry from './ScoreBoardEntry';

interface Props {
  players: Map<string, Character>;
}

const Container = styled.div`
  text-align: center;
  background-color: ${ScoreBoardColors.Background};
  margin-left: 10px;
  width: 300px;
`;

export default class ScoreBoardContainer extends React.Component<Props> {
  public render() {
    return (
      <Container>
        <Header label={'Score'} />
        <Indent>{this.getPlayers()}</Indent>
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
