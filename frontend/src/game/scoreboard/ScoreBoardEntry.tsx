import * as React from 'react';
import styled from "styled-components";
import { TextLabel } from "../../common/TextLabel";
import { Character } from '../type';

interface Props {
  player: Character;
}

const EntryContainer = styled.div`
  padding-bottom: 10%;
  color: ${(props: Props) => props.player.colour};
`;

export default class ScoreBoardEntry extends React.Component<Props> {

  public shouldComponentUpdate(nextProps: Props) {
    return nextProps.player.points !== this.props.player.points;
  }

  public render() {
    const { player } = this.props;
    const playerNameWithScore = `${player.name} ${player.points}`;
    return (
      <EntryContainer player={player}>
        <TextLabel style={{ fontWeight: 'bold' }}>
          {playerNameWithScore}
        </TextLabel>
      </EntryContainer>);
  }
};
