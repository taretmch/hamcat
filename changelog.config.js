module.exports = {
  disableEmoji: false,
  format: '{type}{scope}: {subject}',
  list: ['chore', 'ci', 'debug', 'deps', 'docs', 'feat', 'fix', 'perf', 'refactor', 'release', 'test'],
  maxMessageLength: 80,
  minMessageLength: 3,
  questions: ['type', 'scope', 'subject', 'body', 'breaking'],
  scopes: [
    '',
    'sbt',
    'docs',
    'data',
    'arrow',
    'instance',
    'syntax',
    'util',
  ],
  types: {
    chore: {
      description: 'Build process or auxiliary tool changes',
      emoji: '🤖',
      value: 'chore'
    },
    ci: {
      description: 'CI related changes',
      emoji: '🎡',
      value: 'ci'
    },
    debug: {
      description: 'Debugging code',
      emoji: '🐞',
      value: 'debug'
    },
    deps: {
      description: 'Update dependencies',
      emoji: '📦',
      value: 'deps'
    },
    docs: {
      description: 'Documentation only changes',
      emoji: '✏️',
      value: 'docs'
    },
    feat: {
      description: 'A new feature',
      emoji: '🎸',
      value: 'feat'
    },
    fix: {
      description: 'A bug fix',
      emoji: '🐛',
      value: 'fix'
    },
    perf: {
      description: 'A code change that improves performance',
      emoji: '⚡️',
      value: 'perf'
    },
    refactor: {
      description: 'A code change that neither fixes a bug or adds a feature',
      emoji: '💡',
      value: 'refactor'
    },
    release: {
      description: 'Create a release commit',
      emoji: '🏹',
      value: 'release'
    },
    test: {
      description: 'Adding missing tests',
      emoji: '💍',
      value: 'test'
    },
  }
};
