import * as React from 'react';
import styled from 'styled-components';
import { StandardColors } from '../../common/Constants';
import { DefaultText } from '../../common/DefaultText';
import { Row } from '../../common/Row';
import { Spacing } from '../../common/Spacing';
import { Character } from '../type';

interface Props {
  player: Character;
}

interface ScoreLabelContainer {
  playerColour: string;
}

const ScoreLabelContainer = styled.div`
  opacity: 1;
  color: ${(props: ScoreLabelContainer) => props.playerColour};
  font-size: 32px;
  transition: position 0.5s linear;
`;

export default class ScoreBoardEntry extends React.Component<Props> {
  public shouldComponentUpdate(nextProps: Props) {
    return nextProps.player.points !== this.props.player.points;
  }

  public render() {
    const { player } = this.props;
    const playerName = player.name;
    const playerScore = `${player.points}`;
    return (
      <ScoreLabelContainer playerColour={player.colour}>
        <Spacing num={3}>
          <Row justifyContent={'space-between'} style={styles.row}>
            <DefaultText style={styles.name}>{playerName}</DefaultText>
            <DefaultText style={styles.points}>{playerScore}</DefaultText>
          </Row>
        </Spacing>
      </ScoreLabelContainer>
    );
  }
}

const styles = {
  name: {
    fontWeight: 'bold',
    padding: '10px',
    textOverflow: 'ellipsis',
    overflow: 'hidden',
    maxWidth: '300px',
    whiteSpace: 'nowrap',
    fontSize: '20px',
    height: '30px',
    lineHeight: '30px',
    textAlign: 'left',
  },
  points: {
    fontWeight: 'bold',
    padding: '10px',
    height: '30px',
    lineHeight: '30px',
  },
  row: {
    display: 'flex',
    backgroundColor: StandardColors.Black,
    borderRadius: '5px',
  },
};
