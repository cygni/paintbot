import * as React from 'react';
import styled from "styled-components";
import { TextLabel } from "../../common/TextLabel";
import { Character } from '../type';

interface Props {
  player: Character;
}

interface ScoreLabelContainer {
    playerColour: string;
}

const ScoreLabelContainer = styled.div`
  padding-bottom: 10%;
  color: ${(props: ScoreLabelContainer) => props.playerColour};
`;

export default class ScoreBoardEntry extends React.Component<Props> {

  public shouldComponentUpdate(nextProps: Props) {
    return nextProps.player.points !== this.props.player.points;
  }

  public render() {
    const { player } = this.props;
    const playerNameWithScore = `${player.name} ${player.points}`;
    return (

      <ScoreLabelContainer playerColour={player.colour}>
        <TextLabel style={{fontWeight: 'bold'}}>
            {playerNameWithScore}
        </TextLabel>
    </ScoreLabelContainer>);
  };
}
