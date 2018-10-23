const createMockedPlayers = (nbrOfPlayers: number) => {
  const players = [];
  for (let i = 0; i < nbrOfPlayers; i++) {
    players.push({
      id: `${i}`,
      points: i + 1,
    });
  }

  return players.sort(() => Math.random() - 0.5);
};

export { createMockedPlayers };
