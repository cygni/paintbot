import enzyme from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import 'jest-canvas-mock';

enzyme.configure({ adapter: new Adapter() });
