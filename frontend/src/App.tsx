import * as React from 'react';
import './App.css';

import Game from './game/Game';

class App extends React.Component {
  public render() {
    return (
      <div className="App">
          <Game />
      </div>
    );
  }
}

export default App;
