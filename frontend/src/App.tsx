import * as React from 'react';
import './App.css';
import GameContainer from './game/GameContainer';

class App extends React.Component {
  public render() {
    return (
      <div className="App">
          <GameContainer />
      </div>
    );
  }
}

export default App;
