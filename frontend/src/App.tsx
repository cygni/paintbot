import * as React from 'react';
import './App.css';

import Game from './game/Game';

class App extends React.Component {
  public render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">Welcome to xyz-bot!</h1>
        </header>
          <Game />
      </div>
    );
  }
}

export default App;
