import { shallow } from 'enzyme';
import * as React from 'react';
import ScoreBoardContainer from '../ScoreBoardContainer';
import ScoreBoardEntry from '../ScoreBoardEntry';

describe('Verify ScoreBoardContainer', () => {
  it('should not create players if characters is empty', () => {
    const characters = new Map();
    const wrapper = shallow(<ScoreBoardContainer players={characters} />);
    expect(wrapper.find(ScoreBoardEntry)).toHaveLength(0);
  });
});
