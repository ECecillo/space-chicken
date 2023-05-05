const path = require('path');
const ESLintPlugin = require('eslint-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const webpack = require('webpack');

require('dotenv').config();

module.exports = {
  entry: {
    index: './src/index.ts',
    admin: './src/admin.ts',
  },

  output: {
    path: path.resolve('public/dist/js'),
    filename: '[name].bundle.js',
  },

  module: {
    rules: [
      {
        test: /\.css$/,
        use: [MiniCssExtractPlugin.loader, 'css-loader'],
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
      },
      {
        test: /\.(js|tsx?)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-typescript'],
          },
        },
      },
    ],
  },

  experiments: {
    topLevelAwait: true,
  },

  plugins: [
    new ESLintPlugin({
      extensions: ['js', 'jsx', 'ts', 'tsx'],
    }),
    new MiniCssExtractPlugin({
      filename: '../css/[name].css',
    }),
    new webpack.DefinePlugin({
      'process.env.API_PATH': JSON.stringify(
        process.env.API_PATH || 'http://localhost:3376',
      ),
      'process.env.AUTH_PATH': JSON.stringify(
        process.env.AUTH_PATH || 'http://localhost:8080',
      ),
    }),
  ],

  resolve: {
    extensions: ['.ts', '.js'],
  },

  devServer: {
    static: {
      directory: path.join(__dirname, 'public'),
    },
    compress: true,
    port: 3003,
  },
};
