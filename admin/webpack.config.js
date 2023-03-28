const path = require('path');
const ESLintPlugin = require('eslint-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
  entry: {
    index: './src/index.js',
    admin: './src/admin.js',
  },

  output: {
    path: path.resolve('public/dist/js'),
    filename: '[name].bundle.js',
  },

  module: {
    rules: [
      {
        test: /\.css$/,
        use: [MiniCssExtractPlugin.loader, "css-loader"],
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
      },
    ],
  },

  experiments: {
    topLevelAwait: true
  },

  plugins: [
    new ESLintPlugin({
      extensions: ['js', 'jsx', 'ts', 'tsx'],
      },
    ),
    new MiniCssExtractPlugin({
      filename: "../css/[name].css",
      },
    ),
  ],

  devServer: {
    static: {
      directory: path.join(__dirname, 'public'),
    },
    compress: true,
    port: 3000,
  },
};