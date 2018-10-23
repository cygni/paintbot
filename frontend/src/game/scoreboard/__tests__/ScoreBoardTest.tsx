import { shallow } from 'enzyme';
import * as React from 'react';
import { Character } from '../../type';
import { createMockedPlayers } from '../__mocks__/PlayersMock';
import ScoreBoardContainer from '../ScoreBoardContainer';
import ScoreBoardEntry from '../ScoreBoardEntry';
import { SortOrder, sortPlayers } from '../Util';

describe('Verify ScoreBoardContainer', () => {
  it('should not create players if characters is empty', () => {
    const characters: Character[] = [];
    const wrapper = shallow(<ScoreBoardContainer players={characters} />);
    expect(wrapper.find(ScoreBoardEntry)).toHaveLength(0);
  });
});

describe('Verify Util Functions', () => {
  const playerArray = createMockedPlayers(5) as Character[];
  it('should sort array of players in ascending order', () => {
    const sortedPlayers = sortPlayers(playerArray, SortOrder.ASCENDING);
    let min = sortedPlayers[0].points;
    const invalidValues = [];
    for (let i = 0; i < sortedPlayers.length - 1; i++) {
      if (sortedPlayers[i + 1].points < min) {
        invalidValues.push(sortedPlayers[i + 1]);
      }
      min = sortedPlayers[i + 1].points;
    }
    expect(invalidValues).toHaveLength(0);
  });
  it('should sort array of players in descending order', () => {
    const sortedPlayers = sortPlayers(playerArray, SortOrder.DESCENDING);
    let max = sortedPlayers[0].points;
    const invalidValues = [];
    for (let i = 0; i < sortedPlayers.length - 1; i++) {
      if (sortedPlayers[i + 1].points > max) {
        invalidValues.push(sortedPlayers[i + 1]);
      }
      max = sortedPlayers[i + 1].points;
    }
    expect(invalidValues).toHaveLength(0);
  });
});
