import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:url_launcher/url_launcher.dart';

void main() async {
  await dotenv.load();
  runApp(const SearchEngineApp());
}

class SearchEngineApp extends StatelessWidget {
  const SearchEngineApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: const SearchPage(),
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: Colors.green,
        brightness: Brightness.dark,
      ),
    );
  }
}

class SearchPage extends StatefulWidget {
  const SearchPage({super.key});

  @override
  State<SearchPage> createState() => _SearchPageState();
}

class _SearchPageState extends State<SearchPage> {
  final TextEditingController _searchController = TextEditingController();
  List<Webpage> searchResults = <Webpage>[];

  Future<void> _performSearch(String query) async {
    final List<Webpage>? results = await fetchResults(query);

    setState(() {
      searchResults = results!;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('HyperWeb'),
      ),
      body: Column(
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.only(top: 16, bottom: 16),
            child: FractionallySizedBox(
              widthFactor: 0.5,
              child: TextField(
                controller: _searchController,
                decoration: InputDecoration(
                  border: const OutlineInputBorder(),
                  labelText: 'Search',
                  suffixIcon: IconButton(
                    icon: const Icon(Icons.search),
                    onPressed: () {
                      _performSearch(_searchController.text);
                    },
                  ),
                ),
                onSubmitted: (String value) => _performSearch(value),
              ),
            ),
          ),
          Expanded(
            child: ListView.builder(
              shrinkWrap: true,
              itemCount: searchResults.length,
              itemBuilder: (BuildContext context, int index) {
                return GestureDetector(
                  onTap: () => launchUrl(Uri.parse(searchResults[index].url),
                      webOnlyWindowName: '_blank'),
                  child: FractionallySizedBox(
                    widthFactor: 0.5,
                    child: Card(
                      child: Column(
                        children: <Widget>[
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text(
                              searchResults[index].url,
                              overflow: TextOverflow.fade,
                              softWrap: false,
                              style: const TextStyle(
                                  fontSize: 18,
                                  decoration: TextDecoration.underline,
                                  color: Colors.blue),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text(
                                getContext(searchResults[index].text,
                                    _searchController.text),
                                style: const TextStyle(fontSize: 16)),
                          )
                        ],
                      ),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

Future<List<Webpage>?> fetchResults(String query) async {
  final String gateway = dotenv.env['GATEWAY_HOST']!;
  final http.Response response = await http.get(
      Uri.http('$gateway:8081/', 'SEARCH-API/', <String, dynamic>{'search': query}));

  if (response.statusCode == 200) {
    final List<dynamic> results = jsonDecode(response.body) as List<dynamic>;
    return results.map((dynamic result) {
      return Webpage.fromJson(result as Map<String, dynamic>);
    }).toList();
  } else {
    throw Exception('Failed to load results. Response: $response');
  }
}

class Webpage {
  const Webpage({required this.url, required this.text});

  factory Webpage.fromJson(Map<String, dynamic> json) {
    return Webpage(
      url: json['url'] as String,
      text: json['text'] as String,
    );
  }

  final String url;
  final String text;
}

String getContext(String text, String query) {
  final List<String> words =
      query.split(RegExp(r'\s+')).map((String e) => e.toLowerCase()).toList();

  // Find position of first word
  String expressionFirst = r'\b';
  expressionFirst += words.map((String word) => '($word)').join('.*?');
  expressionFirst += r'\b';

  // Find position of last word
  String expressionLast = r'\b';
  expressionLast += words.reversed.map((String word) => '($word)').join('.*?');
  expressionLast += r'\b';

  final RegExp expFirst = RegExp(expressionFirst, caseSensitive: false);
  final RegExp expLast = RegExp(expressionLast, caseSensitive: false);

  final Match? matchFirst = expFirst.firstMatch(text);
  final Match? matchLast = expLast.firstMatch(text);

  if (matchFirst != null &&
      matchLast != null &&
      matchFirst.start <= matchLast.start) {
    return text.substring(matchFirst.start, matchLast.end).trim();
  } else {
    return '';
  }
}
